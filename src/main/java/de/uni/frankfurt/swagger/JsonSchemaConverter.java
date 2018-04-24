package de.uni.frankfurt.swagger;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import de.uni.frankfurt.json.annotations.JsonField;
import de.uni.frankfurt.json.annotations.JsonObject;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.media.Discriminator;
import io.swagger.v3.oas.models.media.Schema;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonSchemaConverter extends ModelResolver {
  public JsonSchemaConverter(ObjectMapper mapper) {
    super(mapper);
  }

  @Override
  protected String resolveDescription(
      Annotated ann) {
    JsonField jsonField = getJsonField(ann);
    if (jsonField != null) {
      return jsonField.description();
    }
    JsonObject jsonObject = getJsonObject(ann);
    if (jsonObject != null) {
      return jsonObject.description();
    }
    return super.resolveDescription(ann);
  }

  @Override
  protected String resolveTitle(
      Annotated a) {
    JsonField jsonField = getJsonField(a);
    if (jsonField != null) {
      return jsonField.title();
    }
    JsonObject jsonObject = getJsonObject(a);
    if (jsonObject != null) {
      return jsonObject.title();
    }
    return super.resolveTitle(a);
  }

  @Override
  protected String resolveFormat(
      Annotated a) {
    return super.resolveFormat(a);
  }

  @Override
  protected Object resolveExample(
      Annotated a) {
    JsonObject jsonObject = getJsonObject(a);
    if (jsonObject != null) {
      return jsonObject.example().isEmpty() ? null : jsonObject.example();
    }
    return super.resolveExample(a);
  }

  @Override
  protected Boolean resolveReadOnly(
      Annotated a) {
    JsonField schema = getJsonField(a);
    if (schema != null) {
      return schema.readOnly();
    }
    return super.resolveReadOnly(a);
  }

  @Override
  protected Boolean resolveNullable(
      Annotated a) {
    return super.resolveNullable(a);
  }

  @Override
  protected BigDecimal resolveMultipleOf(
      Annotated a) {
    return super.resolveMultipleOf(a);
  }

  @Override
  protected Integer resolveMaxLength(
      Annotated a) {
    JsonField schema = getJsonField(a);
    if (schema != null) {
      return schema.maxLength() == 0 ? null : schema.maxLength();
    }
    return super.resolveMaxLength(a);
  }

  @Override
  protected Integer resolveMinLength(
      Annotated a) {
    JsonField schema = getJsonField(a);
    if (schema != null) {
      return schema.minLength() == 0 ? null : schema.minLength();
    }
    return super.resolveMaxLength(a);
  }

  @Override
  protected BigDecimal resolveMinimum(
      Annotated a) {
    return super.resolveMinimum(a);
  }

  @Override
  protected BigDecimal resolveMaximum(
      Annotated a) {
    JsonField schema = getJsonField(a);
    if (schema != null) {
      return schema.maximum() == 0 ?
          null :
          BigDecimal.valueOf(schema.maximum());
    }
    return super.resolveMaximum(a);
  }

  @Override
  protected Boolean resolveExclusiveMinimum(
      Annotated a) {
    return super.resolveExclusiveMinimum(a);
  }

  @Override
  protected Boolean resolveExclusiveMaximum(
      Annotated a) {
    return super.resolveExclusiveMaximum(a);
  }

  @Override
  protected String resolvePattern(
      Annotated a) {
    JsonField schema = getJsonField(a);
    if (schema != null) {
      return schema.pattern();
    }
    return super.resolvePattern(a);
  }

  @Override
  protected Integer resolveMinProperties(
      Annotated a) {
    return super.resolveMinProperties(a);
  }

  @Override
  protected Integer resolveMaxProperties(
      Annotated a) {
    return super.resolveMaxProperties(a);
  }

  @Override
  protected List<String> resolveRequiredProperties(
      Annotated a) {
    // only json schema annotated object
    if (a.hasAnnotation(JsonObject.class)) {
      ArrayList<String> result = new ArrayList<>();
      // iterate over props
      for (AnnotatedField annotatedField : ((AnnotatedClass) a).fields()) {
        // every annotated json schema prop
        JsonField schema = getJsonField(annotatedField);
        // add to required list when annotated with required
        if (schema != null && schema.required()) {
          result.add(annotatedField.getName());
        }
      }
      return result;
    }
    return super.resolveRequiredProperties(a);
  }

  @Override
  protected Boolean resolveWriteOnly(
      Annotated a) {
    JsonField schema = getJsonField(a);
    if (schema != null) {
      return schema.writeOnly();
    }
    return super.resolveWriteOnly(a);
  }

  /**
   * aka enum
   *
   * @param a annotatedField
   * @return list of string
   */
  @Override
  protected List<String> resolveAllowableValues(
      Annotated a) {
    JsonField schema = getJsonField(a);
    if (schema != null) {
      return Arrays.asList(schema.enumerable());
    }
    return super.resolveAllowableValues(a);
  }

  @Override
  protected Discriminator resolveDiscriminator(
      JavaType type, ModelConverterContext context) {
    return super.resolveDiscriminator(type, context);
  }

  @Override
  protected void resolveSchemaMembers(
      Schema schema, Annotated a) {
    super.resolveSchemaMembers(schema, a);

    Boolean unique = resolveUnique(a);
    if (unique) {
      schema.setUniqueItems(true);
    }
  }

  private Boolean resolveUnique(Annotated a) {
    JsonField schema = getJsonField(a);
    if (schema != null) {
      return schema.uniqueItems();
    }
    return false;
  }

  private JsonField getJsonField(Annotated a) {
    if (a.hasAnnotation(JsonField.class)) {
      return a.getAnnotation(JsonField.class);
    }
    return null;
  }

  private JsonObject getJsonObject(Annotated a) {
    if (a.hasAnnotation(JsonObject.class)) {
      return a.getAnnotation(JsonObject.class);
    }
    return null;
  }
}